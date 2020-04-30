import styled, { css } from 'styled-components'
import { mb, p } from 'styled-components-spacing/dist/cjs'

const square = css`
  width: 50px;
  height: 50px;
  margin: 0;
  padding: 0;
`

const center = css`
  display: flex;
  justify-content: center;
  align-content: center;
`

const Box = styled('div')`
  background-color: ${props => props.theme.colors.primary.two};
  border-radius: 8px;
  width: 100%;

  ${mb(4)};
  ${p({ xs: 2, sm: 3, md: 4 })};

  ${props => props.square && square};
  ${props => props.center && center};
`

export default Box
