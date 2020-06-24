import styled from 'styled-components'
import { p } from 'styled-components-spacing/dist/cjs'

const Popup = styled('div')`
  position: absolute;
  left: 30px;
  top: 30px;
  width: 250px;
  ${p(3)};
  background-color: ${props => props.theme.colors.primary.two};
  cursor: default;
  border-radius: ${props => props.theme.border.radius};
  border-top-left-radius: 0;
  z-index: 1;
`

export default Popup
