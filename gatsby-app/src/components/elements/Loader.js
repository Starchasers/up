import React, { useContext } from 'react'
import { theme } from '../theme'
import AfterUploadBox from '../blocks/AfterUploadBox'
import { CircularProgressbar } from 'react-circular-progressbar'
import 'react-circular-progressbar/dist/styles.css'
import styled from 'styled-components'
import { LoadingContext } from '../../providers/loading-provider'

const CustomLoader = styled(CircularProgressbar)`
  max-width: 50%;
  margin: 0 auto;
  
  >.CircularProgressbar-text {
    dominant-baseline: central;
    fill: ${theme.colors.text.one};
    font-size: 15px;
  }
  
  >.CircularProgressbar-background {
    fill: ${theme.colors.secondary.two};
  }
  
  >.CircularProgressbar-trail {
    stroke: ${theme.colors.secondary.two};
  }
  
  >.CircularProgressbar-path {
    stroke: ${theme.colors.secondary.one};
  }
`

const Loader = () => {
  const { loading } = useContext(LoadingContext)
  return (
    <AfterUploadBox>
      <CustomLoader
        value={loading.value}
        text={`${loading.value === 100 ? 'Saving...' : loading.value + '%'}`}
        background
      />
    </AfterUploadBox>
  )
}

export default Loader
