import React from 'react'
import { theme } from '../theme'
import AfterUploadBox from '../blocks/AfterUploadBox'
import { CircularProgressbar } from 'react-circular-progressbar'
import 'react-circular-progressbar/dist/styles.css'
import styled from 'styled-components'
import { useSelector } from 'react-redux'

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
  const loadingValue = useSelector(state => state.loading.value)
  return (
    <AfterUploadBox>
      <CustomLoader
        value={loadingValue}
        text={`${loadingValue === 100 ? 'Saving...' : loadingValue + '%'}`}
        background
      />
    </AfterUploadBox>
  )
}

export default Loader
