import styled from 'styled-components'
import { p } from 'styled-components-spacing/dist/cjs'

const DropZone = styled('div')`
  border: 3px dashed hsla(0,0%,100%,.4);
  border-radius: ${props => props.theme.border.radius};
  width: 100%;
  ${p(5)};
`

export default DropZone
