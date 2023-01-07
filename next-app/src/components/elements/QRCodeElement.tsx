import { css } from '@emotion/css'
import { QRCode } from 'react-qrcode-logo'
import React from 'react'
import { IProps as IQRCodeElementProps } from 'react-qrcode-logo/lib/index'
import merge from 'lodash/merge'
import theme from '../../assets/theme'

interface IRequiredQRElementProps extends IQRCodeElementProps {
  size: number
  eyeRadius: number
  qrStyle: 'squares' | 'dots'
  ecLevel: 'L' | 'M' | 'Q' | 'H'
  quietZone: number
}

const defaultProps: IRequiredQRElementProps = {
  size: 120,
  eyeRadius: 0,
  qrStyle: 'squares',
  ecLevel: 'L',
  quietZone: 4
}

const QRCodeElement = (props: IQRCodeElementProps) => {
  const _defaultProps = merge(defaultProps, props)

  return (
    <div
      className={css`
        margin: 0 auto;
        opacity: 0.9;
        border-radius: 8px;
        overflow: hidden;
        height: ${_defaultProps.size + 8}px;
        width: ${_defaultProps.size + 8}px;

        @media (max-width: ${theme.breakpoints.sm}) {
          margin: 0 auto;
        }
      `}
    >
      <QRCode {..._defaultProps} />
    </div>
  )
}

export default QRCodeElement
