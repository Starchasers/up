import { css } from '@emotion/css'
import { faCog } from '@fortawesome/free-solid-svg-icons'
import React, { useContext, useMemo, useState } from 'react'
import SideBoxButton from '../SideBoxButton'
import Icon from '../Icon'
import { UploadBoxContentContext } from '../../../providers/UploadBoxContentProvider'
import { Box } from '../../../../@types/box'
import theme from '../../../assets/theme'
import hexToRGB from '../../../utils/hexToRGB'

const ConfigurationMenu = () => {
  const { currentBox } = useContext(UploadBoxContentContext)
  const [open, setOpen] = useState(false)

  const possibleToShow = useMemo(() => {
    return currentBox === Box.DefaultUploadBox
  }, [currentBox])

  return (
    <div
      className={css`
        position: absolute;
        top: 15px;
        right: ${possibleToShow ? (open ? '-215px' : 0) : '50px'};
        bottom: 70px;
        transition: all 350ms;
      `}
    >
      <div
        className={css`
          display: grid;
          position: relative;
          height: 100%;
        `}
      >
        <div
          className={css`
            background: #434755;
            color: ${theme.colors.upBase02};
            padding: 25px;
            height: calc(100% - 50px);
            box-shadow: 8px 8px 0px #1f2229;
            border-radius: 8px;
            width: 150px;
            opacity: ${possibleToShow ? (open ? 1 : 0.75) : 0.75};
            transition: all 250ms;
          `}
        >
          <span>Awesome</span>
          <span> </span>
          <span>Our future config panel</span>
        </div>
        <div
          className={css`
            position: absolute;
            top: 16px;
            right: 0;
            display: grid;
            height: 35px;
            z-index: 0;
            margin-right: -40px;
          `}
        >
          <SideBoxButton
            onClick={() => setOpen(!open)}
            background={open ? '#434755' : theme.colors.upPrimary}
            backgroundHover={open ? hexToRGB('#434755', 0.8) : theme.colors.upPrimaryHover}
          >
            <Icon icon={faCog} />
          </SideBoxButton>
        </div>
      </div>
    </div>
  )
}

export default ConfigurationMenu
