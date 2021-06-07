#version 150 core

uniform sampler2D tex;

in vec4 pass_Color;
in vec2 pass_TexCoord;
in float pass_Intensity;

out vec4 out_Color;

void main(void) 
{
    vec4 color = pass_Color;
        
    // color/texture blending
    color *= texture(tex, pass_TexCoord);

    out_Color = color;
}