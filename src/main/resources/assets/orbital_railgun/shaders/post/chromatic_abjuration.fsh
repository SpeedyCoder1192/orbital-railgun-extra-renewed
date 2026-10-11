#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D DiffuseSampler;
layout(std140) uniform RailgunConfig {
    mat4 InverseTransformMatrix;
    float iTime;
    float IsBlockHit;
    vec3 CameraPosition;
    vec3 BlockPosition;
};

layout(location = 0) in vec2 texCoord;
layout(location = 1) in float viewHeight;
layout(location = 2) in float viewWidth;

layout(location = 0) out vec4 fragColor;

vec2 rotate(vec2 p, float r) {
    return mat2(cos(r), -sin(r), sin(r), cos(r)) * p;
}

void main() {
    float frameTimeCounter = max(iTime - 37., 0.);

    vec3 original = texture(DiffuseSampler, texCoord).rgb;
    vec2 one_pixel = vec2(1. / viewWidth, 1. / viewHeight);
    vec2 rotated_pixel = rotate(one_pixel, -frameTimeCounter);

    float scale = max((-pow((frameTimeCounter - 0.84) * 8., 2.) + 50.) * 25. / (distance(CameraPosition, BlockPosition) - 24. + 25.), 0.);
    float ca_red = texture(DiffuseSampler, texCoord + (rotated_pixel) * scale).r;
    rotated_pixel = rotate(rotated_pixel, 2.09439510239);
    float ca_green = texture(DiffuseSampler, texCoord + (rotated_pixel - one_pixel) * scale).g;
    rotated_pixel = rotate(rotated_pixel, 2.09439510239);
    float ca_blue = texture(DiffuseSampler, texCoord + (rotated_pixel - one_pixel) * scale).b;

    fragColor = vec4(mix(original, vec3(ca_red, ca_green, ca_blue), 2. * length(texCoord - vec2(0.5))), 1.);
}
