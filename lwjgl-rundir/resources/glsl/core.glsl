
// eaglercraft opengl 1.3 emulation
// copyright (c) 2020 calder young
// creative commons BY-NC 4.0

#line 7

precision highp int;
precision highp sampler2D;
precision highp float;

uniform mat4 matrix_m;
uniform mat4 matrix_p;
uniform mat4 matrix_t;

#ifdef CC_VERT

in vec3 a_position;
#ifdef CC_a_texture0
in vec2 a_texture0;
#endif
#ifdef CC_a_color
in vec4 a_color;
#endif
#ifdef CC_a_normal
in vec4 a_normal;
#endif
#ifdef CC_a_texture1
in vec2 a_texture1;
#endif

#ifdef CC_TEX_GEN_STRQ
out vec4 v_object_pos;
#endif
out vec4 v_position;
#ifdef CC_a_color
out vec4 v_color;
#endif
#ifdef CC_a_normal
out vec4 v_normal;
#endif
#ifdef CC_a_texture0
out vec2 v_texture0;
#endif
#ifdef CC_a_texture1
out vec2 v_texture1;
#endif

#endif


#ifdef CC_VERT

void main(){
	vec4 pos = matrix_m * vec4(a_position, 1.0);
	v_position = pos;
#ifdef CC_TEX_GEN_STRQ
	v_object_pos = vec4(a_position, 1.0);
#endif
#ifdef CC_a_color
	v_color = a_color;
#endif
#ifdef CC_a_normal
	v_normal = a_normal;
#endif
#ifdef CC_a_texture0
	v_texture0 = a_texture0;
#endif
#ifdef CC_a_texture1
	v_texture1 = a_texture1;
#endif
	gl_Position = matrix_p * pos;
}

#endif

#ifdef CC_FRAG

uniform sampler2D tex0;
uniform sampler2D tex1;
uniform vec2 texCoordV0;
uniform vec2 texCoordV1;
#ifdef CC_lighting
uniform vec3 light0Pos;
uniform vec3 light1Pos;
uniform vec3 normalUniform;
#endif
#ifdef CC_fog
uniform vec4 fogColor;
uniform int fogMode;
uniform float fogStart;
uniform float fogEnd;
uniform float fogDensity;
uniform float fogPremultiply;
#endif
uniform vec4 colorUniform;
#ifdef CC_alphatest
uniform float alphaTestF;
#endif
#ifdef CC_TEX_GEN_STRQ
uniform int textureGenS_M;
uniform int textureGenT_M;
uniform int textureGenR_M;
uniform int textureGenQ_M;
uniform vec4 textureGenS_V;
uniform vec4 textureGenT_V;
uniform vec4 textureGenR_V;
uniform vec4 textureGenQ_V;
#endif
#ifdef CC_patch_anisotropic
uniform vec2 anisotropic_fix;
#endif

#ifdef CC_TEX_GEN_STRQ
in vec4 v_object_pos;
#endif
in vec4 v_position;
#ifdef CC_a_color
in vec4 v_color;
#endif
#ifdef CC_a_normal
in vec4 v_normal;
#endif
#ifdef CC_a_texture0
in vec2 v_texture0;
#endif
#ifdef CC_a_texture1
in vec2 v_texture1;
#endif

out vec4 fragColor;

void main(){
#ifdef CC_a_color
	vec4 color = colorUniform * v_color;
#else
	vec4 color = colorUniform;
#endif
	
#ifdef CC_TEX_GEN_STRQ
	vec4 texSrc[2];
	texSrc[0] = v_object_pos;
	texSrc[1] = v_position;
	
	vec4 texPos;
	texPos.x = dot(texSrc[textureGenS_M], textureGenS_V);
	texPos.y = dot(texSrc[textureGenT_M], textureGenT_V);
	texPos.z = dot(texSrc[textureGenR_M], textureGenR_V);
	texPos.w = dot(texSrc[textureGenQ_M], textureGenQ_V);
	
	texPos = matrix_t * texPos;
	
	color *= texture(tex0, texPos.xy / texPos.w).bgra;
#ifdef CC_alphatest
	if(color.a < alphaTestF){
		discard;
	}
#endif
#else
#ifdef CC_unit0
#ifdef CC_a_texture0

#ifdef CC_patch_anisotropic
	vec2 uv = (matrix_t * vec4(v_texture0, 0.0, 1.0)).xy;
	
	/* https://bugs.chromium.org/p/angleproject/issues/detail?id=4994 */
	uv = ((uv * anisotropic_fix) - fract(uv * anisotropic_fix) + 0.5) / anisotropic_fix;
	
	vec4 texColor = texture(tex0, uv);
#else
	vec4 texColor = texture(tex0, (matrix_t * vec4(v_texture0, 0.0, 1.0)).xy);
#endif

#else
	vec4 texColor = texture(tex0, (matrix_t * vec4(texCoordV0, 0.0, 1.0)).xy);
#endif

#ifdef CC_swap_rb
	color *= texColor.rgba;
#else
	color *= texColor.bgra;
#endif

#endif

#ifdef CC_alphatest
	if(color.a < alphaTestF){
		discard;
	}
#endif

#ifdef CC_unit1
#ifdef CC_a_texture1
	color.rgb *= texture(tex1, (v_texture1 + 8.0) * 0.00390625).bgr;
#else
	color.rgb *= texture(tex1, (texCoordV1 + 8.0) * 0.00390625).bgr;
#endif
#endif
#endif

#ifdef CC_lighting
#ifdef CC_a_normal
	vec3 normal = ((v_normal.xyz - 0.5) * 2.0);
#else
	vec3 normal = normalUniform;
#endif
	normal = normalize(mat3(matrix_m) * normal);
	float ins = max(dot(normal, -light0Pos), 0.0) + max(dot(normal, -light1Pos), 0.0);
	color.rgb *= min((0.4 + ins * 0.6), 1.0);
#endif
	
#ifdef CC_fog
	float dist = sqrt(dot(v_position, v_position));
	float i = fogMode == 1 ? (dist - fogStart) / (fogEnd - fogStart) : 1.0 - pow(2.718, -(fogDensity * dist));
	color.rgb = mix(color.rgb, fogColor.xyz, clamp(i, 0.0, 1.0) * fogColor.a);
#endif
	
	fragColor = color;
}

#endif

