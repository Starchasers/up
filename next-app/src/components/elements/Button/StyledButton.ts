import styled, { css } from 'styled-components'

import { includeVariant } from './variants'

const rounded = css`
  width: 25px;
  height: 25px;
  padding: 10px;
  border-radius: 50%;
`

const StyledButton = styled('div')`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  user-select: none;
  flex-direction: ${props => props.iconAlign === 'left' ? 'row' : 'row-reverse'};

  ${props => includeVariant(props, 'button')};

  ${props => props.rounded && rounded};
`

export default StyledButton
