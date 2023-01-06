import { css } from '@emotion/css'
import React, { useState } from 'react'
import Tooltip from '../components/elements/Tooltip'

const useTooltip = (
  text: string
): [React.Dispatch<React.SetStateAction<boolean>>, React.ReactNode] => {
  const [active, setActive] = useState(false)

  return [
    setActive,
    <div
      className={css`
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        display: flex;
        justify-content: center;
      `}
    >
      <Tooltip active={active}>{text}</Tooltip>
    </div>
  ]
}

export default useTooltip
