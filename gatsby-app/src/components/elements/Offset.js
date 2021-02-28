import styled, { css } from 'styled-components'

const dragActive = css`
  filter: brightness(50%);
`

const Offset = styled('div')`
  padding-top: 17vh;
  min-height: 100vh;
  transition: 500ms all;
  
  &:focus {
    outline: unset;
  }
  
  ${props => props.dragActive && dragActive}
`

export default Offset
