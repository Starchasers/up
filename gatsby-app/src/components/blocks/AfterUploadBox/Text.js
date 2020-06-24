import styled from 'styled-components'
import { breakpoint } from 'styled-components-breakpoint'

const Text = styled('span')`
  font-size: 18px;
  max-width: 100%;
  width: 100%;
  display: flex;
  overflow-x: auto;
  overflow-y: hidden;
  
  ${breakpoint('sm')`
    justify-content: center;
  `}
`

export default Text
