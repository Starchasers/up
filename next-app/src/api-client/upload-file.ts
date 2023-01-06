import axios, { AxiosResponse } from 'axios'

import { IUploadFileRequest, IUploadFileResponse } from '../../@types/api-client/uploadFile'
import { ApiEndpoints } from './const'
import { IAxiosRequest } from '../../@types/axios'

const uploadFile = (
  payload: IUploadFileRequest & IAxiosRequest
): Promise<AxiosResponse<IUploadFileResponse>> => {
  const data = new FormData()
  data.append('file', payload.data.file)
  data.append('expires', payload.data.expires?.toString() ?? undefined)

  return axios.post(ApiEndpoints.uploadFile, data, payload.config)
}

export default uploadFile
