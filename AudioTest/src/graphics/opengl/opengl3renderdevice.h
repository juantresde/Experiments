#ifndef OPENGL_3_RENDER_DEVICE_INCLUDED_H
#define OPENGL_3_RENDER_DEVICE_INCLUDED_H

#include "../irenderdevice.h"

class OpenGL3RenderDevice : public IRenderDevice
{
public:
	OpenGL3RenderDevice();

	virtual IVertexArray* CreateVertexArrayFromFile(const std::string& fileName);
	virtual IVertexArray* CreateVertexArray(const IndexedModel& model);
//	virtual IVertexArray* CreateVertexArray(
//			float** vertexData, unsigned int* vertexElementSizes,
//			unsigned int numVertexComponents, unsigned int numVertices,
//			unsigned int* indices, unsigned int numIndices);
	virtual void ReleaseVertexArray(IVertexArray* vertexArray);

	virtual IShaderProgram* CreateShaderProgram(const std::string& shaderText);
	virtual IShaderProgram* CreateShaderProgramFromFile(
			const std::string& fileName);
	virtual void ReleaseShaderProgram(IShaderProgram* shaderProgram);

	virtual ITexture* CreateTextureFromFile(const std::string& fileName,
			bool compress, int filter, float anisotropy,
			bool clamp);
	virtual ITexture* CreateTexture(int width, int height, unsigned char* data, 
			int format, int internalFormat, bool compress, int filter, 
			float anisotropy, bool clamp);

	virtual void ReleaseTexture(ITexture* texture);

private:
	unsigned int m_version;
	std::string m_shaderVersion;
};

#endif
