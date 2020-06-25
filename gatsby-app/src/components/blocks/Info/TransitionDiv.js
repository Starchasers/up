import styled from 'styled-components'

const TransitionDiv = styled('div')`
  overflow: hidden;
  position: absolute;
  left: 0;
  top: 0;
  width: 0;
  height: 0;
  transition: 200ms all;
  z-index: 0;
  cursor: default;
`

export default TransitionDiv
