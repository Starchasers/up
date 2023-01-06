import styled from '@emotion/styled'
import { css } from '@emotion/react'

const active = (marginRight?: string) => css`
  margin-right: ${marginRight ?? '-45px'};
`

export const MovableElement = styled('div')<{
  delay: string
  active: boolean
  marginRight?: string
}>`
  transition-property: margin-right;
  transition-delay: ${(props) => props.delay};
  transition-duration: 500ms;
  margin-right: 0px;
  ${(props) => props.active && active(props.marginRight)};
`

const SideBoxButton = styled('div')<{ background?: string; backgroundHover?: string }>`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background: ${(props) => props.background ?? props.theme.colors.upPrimary};
  border-top-right-radius: 8px;
  border-bottom-right-radius: 8px;
  //width: 80px;
  transition-property: background;
  cursor: pointer;
  font-size: 17.5px;
  margin-left: auto;
  height: 100%;
  transition: 250ms all;
  min-width: 43px;

  &:hover {
    background: ${(props) => props.backgroundHover ?? props.theme.colors.upPrimaryHover};
    margin-right: -3px;
  }
`

export default SideBoxButton
