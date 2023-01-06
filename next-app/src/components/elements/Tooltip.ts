import { css } from '@emotion/react'
import styled from '@emotion/styled'

const active = css`
  top: -45px;
  opacity: 1;
  z-index: 1;
`

const Tooltip = styled('div')<{ active: boolean }>`
  position: absolute;
  top: 0;
  opacity: 0;
  background-color: ${(props) => props.theme.colors.shark};
  color: ${(props) => props.theme.colors.timberwolf};
  border-radius: 8px;
  font-size: 16px;
  transition: 200ms all;
  z-index: -1;
  padding: 8px;
  margin: 0 auto;

  &::before {
    content: '';
    position: absolute;
    height: 5px;
    width: 5px;
    background: #1f2229;
    transform: rotate(45deg);
    left: 0;
    right: 0;
    margin: 0 auto;
    bottom: -2px;
    z-index: -1;
  }

  ${(props) => props.active && active};
`

export default Tooltip
