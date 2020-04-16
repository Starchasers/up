import styled, { css } from 'styled-components'
import { breakpoint } from 'styled-components-breakpoint'
import { mr, pt } from 'styled-components-spacing/dist/cjs'

const right = css`
  margin-left: auto;
  margin-right: 0;

  ${breakpoint('md')`
    ${mr(5)};
  `};

  ${breakpoint('xs', 'sm')`
    margin-left: 0;
    ${pt(4)};
  `}
`

const light = css`
  color: ${props => props.theme.colors.text.three};
  font-weight: normal;
  font-size: 13px;
`

const Text = styled('span')`
  display: flex;
  align-items: center;
  font-weight: bold;
  font-size: 24px;
  color: ${props => props.theme.colors.text.one};

  ${props => props.right && right};
  ${props => props.light && light};
`

export default Text
