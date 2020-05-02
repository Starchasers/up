import React from 'react'
import { theme } from '../theme'
import AfterUploadBox from '../blocks/AfterUploadBox'
import { CircularProgressbar } from 'react-circular-progressbar'
import 'react-circular-progressbar/dist/styles.css'
import styled from 'styled-components'

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

const Loader = ({ loader, children }) => (
  loader.isLoading
    ? <AfterUploadBox>
      <CustomLoader
        value={loader.value}
        text={`${loader.value === 100 ? 'Saving...' : loader.value + '%'}`}
        background
      />
    </AfterUploadBox>
    : children
)

export default Loader
