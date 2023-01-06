import { css, Theme } from '@emotion/react'
import styled from '@emotion/styled'

import hexToRGB from '../../../../../utils/hexToRGB'
import StyledButton from '../../StyledButton'
import { colorStates } from '../_colorVariants'

const disabledStyle = (props: { theme: Theme; colorStates: colorStates }) => css`
  box-shadow: unset;
  background: ${props.theme.colors[props.colorStates.disabled]};
  border-color: ${props.theme.colors[props.colorStates.disabled]};
  cursor: default;

  &:hover {
    box-shadow: unset;
    background: ${props.theme.colors[props.colorStates.disabled]};
    border-color: ${props.theme.colors[props.colorStates.disabled]};
  }
`

const PrimaryStyledButton = styled(StyledButton)`
  box-shadow: 0 4px 20px 0 ${(props) => hexToRGB(props.theme.colors[props.colorStates.unset], 0.3)};
  border: 2px solid ${(props) => props.theme.colors[props.colorStates.unset]};
  background: ${(props) => props.theme.colors[props.colorStates.unset]};
  padding: 5px 10px;
  border-radius: 8px;
  transition: all ${(props) => props.theme.transitions.normal};
  opacity: 0.7;

  &:hover {
    box-shadow: 0 4px 20px 0
      ${(props) => hexToRGB(props.theme.colors[props.colorStates.hover], 0.3)};
    border: 2px solid ${(props) => props.theme.colors[props.colorStates.hover]};
    background: ${(props) => props.theme.colors[props.colorStates.hover]};
    opacity: 1;
  }

  ${(props) => props.disabled && disabledStyle};
`

export default PrimaryStyledButton
