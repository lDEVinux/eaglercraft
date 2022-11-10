#line 2

precision highp int;
precision highp sampler2D;
precision highp float;

#ifdef CC_VERT
uniform mat4 matrix_m;
uniform mat4 matrix_p;

in vec3 a_vert;

void main(){
	gl_Position = (matrix_p * (matrix_m * vec4(a_vert, 1.0)));
}
#endif

#ifdef CC_FRAG

out vec4 fragColor;

void main(){
	fragColor = vec4(1.0);
}
#endif
