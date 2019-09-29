void main()
{
    fragColor = texture(diffuse, texCoord0.xy) * 
    	calcLightingEffect(normalize(normal0), worldPos0);
}
