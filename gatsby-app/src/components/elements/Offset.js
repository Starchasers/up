import styled, { css } from 'styled-components'

const blur = css`
  filter: blur(8px) brightness(50%);
`

const Offset = styled('div')`
  padding-top: 17vh;
  min-height: 100vh;
  transition: 500ms all;
  
  &:focus {
    outline: unset;
  }
  
  ${props => props.blur && blur};
`

export default Offset
