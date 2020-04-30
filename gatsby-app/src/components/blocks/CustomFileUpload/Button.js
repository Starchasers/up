import styled from 'styled-components'
import { px, py } from 'styled-components-spacing/dist/cjs'

const Button = styled('div')`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  ${px(4)};
  ${py(3)};
`

export default Button
