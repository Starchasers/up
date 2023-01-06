import axios, { AxiosResponse } from 'axios'

import { IDeleteFileRequest, IDeleteFileResponse } from '../../@types/api-client/deleteFile'
import { ApiEndpoints } from './const'
import { IAxiosRequest } from '../../@types/axios'

const deleteFile = (
  payload: IDeleteFileRequest & IAxiosRequest
): Promise<AxiosResponse<IDeleteFileResponse>> => {
  return axios.delete(`${ApiEndpoints.deleteFile}/${payload.key}`, {
    ...payload.config,
    data: payload.data
  })
}

export default deleteFile
