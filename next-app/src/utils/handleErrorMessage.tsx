import axios, { AxiosError } from 'axios'
import { toast } from 'react-toastify'
import { IUpAppUniversalError } from '../../@types/api-client/common'

const handleErrorMessage = (error: AxiosError<IUpAppUniversalError> | Error) => {
  if (axios.isAxiosError(error)) {
    console.error(error.response?.data.message)
    toast(error.response?.data.message, { type: 'error' })
  } else {
    toast(error, { type: 'error' })
  }
}

export default handleErrorMessage
