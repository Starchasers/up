export const backendApiUrl = process.env.NEXT_PUBLIC_BACKEND_API_URL ?? '/api'

export const ApiEndpoints: { [key in string]: string } = {
  uploadFile: `${backendApiUrl}/upload`,
  deleteFile: `${backendApiUrl}/u`, // /key
  getApplicationConfiguration: `${backendApiUrl}/configuration`
}
