#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec4 light_pos;

in vec4 in_Position;
in vec4 in_Normal;
in vec4 in_Color;
in vec2 in_TexCoord;

out vec4 pass_Color;
out vec2 pass_TexCoord;
out float pass_Intensity;


void main(void) 
{
    vec4 light_dir = in_Position - light_pos;
    light_dir = normalize(light_dir);
    vec4 normal = normalize(modelMatrix * in_Normal);
    float NdotL = -dot(normal, light_dir);

    // ambient threshhold
    pass_Intensity = max(NdotL, 0.3);

    // Override gl_Position with our new calculated position
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;

    pass_Color = in_Color;
    pass_TexCoord = in_TexCoord;
}