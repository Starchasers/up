import axios, { AxiosError } from 'axios'
import { toast } from 'react-toastify'
import { ToastOptions } from 'react-toastify/dist/types'
import { IUpAppUniversalError } from '../../@types/api-client/common'

const handleErrorMessage = (
  error: AxiosError<IUpAppUniversalError> | Error,
  options?: ToastOptions
) => {
  if (axios.isAxiosError(error)) {
    toast(error.response?.data.message ?? error.toString(), {
      type: 'error',
      theme: 'dark',
      ...options
    })
  } else {
    toast(error, { type: 'error', theme: 'dark' })
  }
}

export default handleErrorMessage
