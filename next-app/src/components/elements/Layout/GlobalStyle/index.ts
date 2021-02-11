import { createGlobalStyle } from 'styled-components'

import resetStyle from './resetStyle'
import animationsStyle from './animationsStyle'

const GlobalStyle = createGlobalStyle`
  ${resetStyle};
  ${animationsStyle};

  html {
    overflow-y: scroll;
  }

  body {
    font-family: 'Lato', sans-serif;
    font-size: 16px;
    line-height: 1.2;
    color: ${props => props.theme.palette.text};
    overflow-x: hidden;
    font-weight: 400;
  }
`

export default GlobalStyle
