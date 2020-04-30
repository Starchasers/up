import styled from 'styled-components'
import { pr } from 'styled-components-spacing/dist/cjs'

const Text = styled('a')`
  color: ${props => props.theme.colors.text.one};
  text-decoration: none;
  line-height: 25px;
  white-space: nowrap;
  ${pr({ xs: 2, md: 0 })};
  text-align: center;
  
  &:hover {
    text-decoration: underline;
  }
`

export default Text
