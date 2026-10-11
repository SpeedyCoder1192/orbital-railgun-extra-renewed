#version 330
#extension GL_ARB_separate_shader_objects : require

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 DiffuseSize;
};

layout(location = 0) out vec2 texCoord;
layout(location = 1) out float viewHeight;
layout(location = 2) out float viewWidth;

void main() {
    vec2 uv = vec2((gl_VertexIndex << 1) & 2, gl_VertexIndex & 2);
    gl_Position = vec4(uv * vec2(2.0, 2.0) + vec2(-1.0, -1.0), 0.0, 1.0);

    texCoord = uv;
    viewHeight = OutSize.y;
    viewWidth = OutSize.x;
}
