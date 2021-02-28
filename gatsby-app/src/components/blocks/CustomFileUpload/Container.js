import styled from 'styled-components'

import DropZone from './DropZone'

const Container = styled('div')`
  position: relative;
  display: flex;
  cursor: pointer;
  border-radius: ${props => props.theme.border.radius};
  margin: 0 auto;
  align-items: center;
  flex-direction: column;

  &:focus {
    outline: none;
  }

  &:focus, &:active {
    ${DropZone} {
      border-color: rgba(255, 255, 255, 0.6);
    }
  }
`

export default Container
