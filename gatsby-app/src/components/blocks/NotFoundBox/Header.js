import styled from 'styled-components'
import { mb, mt } from 'styled-components-spacing/dist/cjs'

const Header = styled('h1')`
  color: ${props => props.theme.colors.text.one};
  font-family: 'Roboto', sans-serif;
  text-align: center;
  font-size: 40px;
  margin-bottom: 24px;
`

export default Header
