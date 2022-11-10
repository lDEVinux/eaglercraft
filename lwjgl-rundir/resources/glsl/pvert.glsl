#line 0

precision lowp int;
precision lowp sampler2D;
precision lowp float;

in vec2 a_pos;

out vec2 pos;

void main(){
	gl_Position = vec4((pos = a_pos) * 2.0 - 1.0, 0.0, 1.0);
}
