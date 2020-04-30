import CustomFileUpload from './blocks/CustomFileUpload'
import React from 'react'
import Dropzone from 'react-dropzone'
import { faFolderOpen } from '@fortawesome/free-solid-svg-icons'
import axios from 'axios'

const handleFileUpload = async (props) => {
  try {
    props.setLoading(true)

    const data = new FormData()
    data.append('file', props.files[0])

    await axios.post(`${process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : ''}/api/upload`, data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }).then(res => {
      props.setUpload({ uploaded: true, data: { ...res.data } })
    })
  } catch (e) {
    console.log(e)
    props.setUpload({ uploaded: false, data: {} })
  } finally {
    props.setLoading(false)
  }
}

const DropZoneInput = ({ getRootProps, getInputProps }) => (
  <CustomFileUpload.Container {...getRootProps()} style={{ width: '80%' }}>
    <CustomFileUpload.DropZone>
      <CustomFileUpload.Text bold>Drop file here</CustomFileUpload.Text>
    </CustomFileUpload.DropZone>
    <CustomFileUpload.Input {...getInputProps()}/>
  </CustomFileUpload.Container>
)

const ChooseFile = ({ getRootProps, getInputProps }) => (
  <CustomFileUpload.Container {...getRootProps()}>
    <CustomFileUpload.Button>
      <CustomFileUpload.Icon icon={faFolderOpen} style={{ width: '22.5px' }}/>
      <CustomFileUpload.Text>Choose File</CustomFileUpload.Text>
    </CustomFileUpload.Button>
    <CustomFileUpload.Input {...getInputProps()}/>
  </CustomFileUpload.Container>
)

const FileUpload = (props) => (
  <Dropzone onDrop={files => handleFileUpload({ files, ...props })}>
    {(d) => (
      <CustomFileUpload>
        <DropZoneInput {...d} />
        <CustomFileUpload.Or>OR</CustomFileUpload.Or>
        <ChooseFile {...d} />
      </CustomFileUpload>
    )}
  </Dropzone>
)

export default FileUpload
