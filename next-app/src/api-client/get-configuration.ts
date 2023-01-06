import axios, { AxiosResponse } from 'axios'
import {
  IGetConfigurationRequest,
  IGetConfigurationResponse
} from '../../@types/api-client/getConfiguration'
import { ApiEndpoints } from './const'
import { IAxiosRequest } from '../../@types/axios'

const getConfiguration = (
  payload: IGetConfigurationRequest & IAxiosRequest
): Promise<AxiosResponse<IGetConfigurationResponse>> => {
  return axios.get(ApiEndpoints.getApplicationConfiguration, payload.config)
}

export default getConfiguration
