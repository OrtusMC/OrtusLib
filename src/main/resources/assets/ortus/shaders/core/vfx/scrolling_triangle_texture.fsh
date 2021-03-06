#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float GameTime;
uniform float Speed;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    vec2 uv = texCoord0;

    float y = uv.y;
    float width = (1.-y);
    if (abs(uv.x-0.5)*2. > y)
    {
        discard;
    }
    uv.x -= 0.5*width;
    uv.x /= y;
    uv.y += GameTime*Speed;
    vec4 color = texture(Sampler0, uv) * vertexColor;
    fragColor = color * ColorModulator;
}
