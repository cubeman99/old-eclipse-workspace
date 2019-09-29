#version 330



const int MAX_POINT_LIGHTS = 4;
const int MAX_SPOT_LIGHTS  = 4;


// input/output

in vec2 texCoord0;
in vec3 normal0;
in vec3 tangent0;
in mat3 tbnMatrix;
in vec3 worldPos0;

out vec4 fragColor;


// structures

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct DirectionalLight
{
    BaseLight base;
    vec3 direction;
};

struct Attenuation
{
	float constant;
	float linear;
	float exponent;
};

struct PointLight
{
	BaseLight base;
	Attenuation atten;
	vec3 position;
	float range;
};

struct SpotLight
{
	PointLight pointLight;
	vec3 direction;
	float cutoff;
};


// Uniforms

uniform vec3 cameraPos;

uniform sampler2D sampler;   // texture
uniform sampler2D normalMap; // normal map
uniform sampler2D dispMap;   // displacement map
uniform float dispMapScale;
uniform float dispMapBias;

uniform vec3 baseColor;
uniform vec3 ambientLight;
uniform float specularIntensity;
uniform float specularPower;

uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];


vec4 calcLight(BaseLight base, vec3 direction, vec3 normal)
{
    float diffuseFactor = dot(normal, -direction);
    
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);
	
    if(diffuseFactor > 0)
    {
        diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
		
		vec3 directionToCamera = normalize(cameraPos - worldPos0);
		vec3 reflectDirection = normalize(reflect(direction, normal));
		
		float specularFactor = dot(directionToCamera, reflectDirection);
		specularFactor = pow(specularFactor, specularPower);
		
		if (specularFactor > 0)
		{
			specularColor = vec4(base.color, 1.0) * specularIntensity * specularFactor;
		}
    }
    
    return diffuseColor + specularColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal)
{
    return calcLight(directionalLight.base, -directionalLight.direction, normal);
}

vec4 calcPointLight(PointLight pointLight, vec3 normal)
{
	vec3 lightDirection = worldPos0 - pointLight.position;
	float distanceToPoint = length(lightDirection);
	
	if (distanceToPoint > pointLight.range)
		return vec4(0, 0, 0, 0);
	
	lightDirection = normalize(lightDirection);
	
	vec4 color = calcLight(pointLight.base, lightDirection, normal);
	
	float attenuation = pointLight.atten.constant + 
			(pointLight.atten.linear * distanceToPoint) + 
			(pointLight.atten.exponent * distanceToPoint * distanceToPoint) + 
			0.0001; // <--- prevents division by zero
	
	return color / attenuation;
}

vec4 calcSpotLight(SpotLight spotLight, vec3 normal)
{
	vec3 lightDirection = normalize(worldPos0 - spotLight.pointLight.position);
	float spotFactor = dot(lightDirection, spotLight.direction);
	
	vec4 color = vec4(0, 0, 0, 0);
	
	if (spotFactor > spotLight.cutoff)
	{
		color = calcPointLight(spotLight.pointLight, normal) * 
				(1.0 - ((1.0 - spotFactor) / (1.0 - spotLight.cutoff)));
	}
	
	return color;
}

vec4 calcTotalLight(vec3 normal)
{
    vec4 totalLight = vec4(ambientLight, 1);
	
	// Apply directional light.
    totalLight += calcDirectionalLight(directionalLight, normal);
	
	// Apply all point lights.
	for (int i = 0; i < MAX_POINT_LIGHTS; i++)
	{
		if (pointLights[i].base.intensity > 0)
			totalLight += calcPointLight(pointLights[i], normal);
	}
	
	// Apply all spot lights.
	for (int i = 0; i < MAX_SPOT_LIGHTS; i++)
	{
		if (spotLights[i].pointLight.base.intensity > 0)
			totalLight += calcSpotLight(spotLights[i], normal);
	}
	
	return totalLight;
}

vec2 calcParallaxTexCoords(sampler2D dispMap, mat3 tbnMatrix, vec3 directionToCamera,
		vec2 texCoords, float scale, float bias)
{
	return texCoords.xy
		+ (directionToCamera * tbnMatrix).xy
		* (texture2D(dispMap, texCoords.xy).r
		* scale + bias);
}

void main()
{ 
	vec3 directionToCamera = normalize(cameraPos - worldPos0);
	vec2 texCoord = calcParallaxTexCoords(dispMap, tbnMatrix, directionToCamera,
					texCoord0, dispMapScale, dispMapBias);
	
	vec3 normal = normalize(tbnMatrix * ((255.0 / 128.0 * texture2D(normalMap, texCoord).xyz) - 1));

    vec4 color = vec4(baseColor, 1);
    vec4 textureColor = texture(sampler, texCoord.xy);
    
    if(textureColor != vec4(0, 0, 0, 0))
        color *= textureColor;
    
    fragColor = color * calcTotalLight(normal);
}