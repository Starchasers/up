import React, { createContext, useState } from 'react'
import { Box } from '../../@types/box'

export type TUploadBoxContentContext = {
  currentBox: Box
  setCurrentBox: React.Dispatch<Box>
}

export const UploadBoxContentContext = createContext<TUploadBoxContentContext>({
  currentBox: Box.DefaultUploadBox,
  setCurrentBox: () => undefined
})

const UploadBoxContentProvider: React.FC = (props) => {
  const [currentBox, setCurrentBox] = useState<Box>(Box.DefaultUploadBox)

  return (
    <UploadBoxContentContext.Provider value={{ currentBox, setCurrentBox }}>
      {props.children}
    </UploadBoxContentContext.Provider>
  )
}

export default UploadBoxContentProvider
