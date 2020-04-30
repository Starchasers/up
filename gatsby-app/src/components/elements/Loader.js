import 'react-loader-spinner/dist/loader/css/react-spinner-loader.css'
import React from 'react'
import ReactLoader from 'react-loader-spinner'
import { theme } from '../theme'
import AfterUploadBox from '../blocks/AfterUploadBox'

const Loader = ({ loading, children }) => (
  loading
    ? <AfterUploadBox>
        <ReactLoader
          type='Bars'
          color={theme.colors.secondary.one}
          height={100}
          width={100}
          style={{textAlign: 'center'}}
        />
    </AfterUploadBox>
    : children
)

export default Loader
