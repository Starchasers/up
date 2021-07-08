import styled from 'styled-components'
import { p } from 'styled-components-spacing/dist/cjs'

const DropZone = styled('div')`
  border: 3px dashed rgba(255, 255, 255, 0.4);
  border-radius: ${props => props.theme.border.radius};
  width: 100%;
  ${p(5)};
  transition: 300ms all;
`

export default DropZone
