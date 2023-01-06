import { css } from '@emotion/css'
import { faAngleLeft, faCopy } from '@fortawesome/free-solid-svg-icons'
import React from 'react'
import FileUploaded from '../../../components/blocks/Boxes/FileUploadedBox'
import Button from '../../../components/elements/Button'
import Icon from '../../../components/elements/Icon'
import LinkCopyButton from '../../../components/elements/LinkCopyButton'
import ButtonContainer from '../../../components/elements/ButtonContainer'
import TimerCountdown from '../../../components/elements/TimerCountdown'
import theme from '../../../assets/theme'
// import QRCodeElement from '../../../components/elements/QRCodeElement'
import { IUseFileUploadedBoxState } from './useFileUploadedBox'
// import colorVariants from '../../../components/elements/Button/variants/_colorVariants'
import MobileContainer from '../../../components/elements/MobileContainer'
import DesktopContainer from '../../../components/elements/DesktopContainer'

interface IFileUploadedBoxViewProps extends IUseFileUploadedBoxState {}

const FileUploadedBoxView = (props: IFileUploadedBoxViewProps) => (
  <FileUploaded>
    <LinkCopyButton
      link={props.link}
      text={
        <>
          <DesktopContainer breakpoint={theme.breakpoints.sm}>
            <Icon icon={faCopy} />
          </DesktopContainer>
          <MobileContainer breakpoint={theme.breakpoints.sm}>
            <span>Copy URL</span>
            <Icon icon={faCopy} />
          </MobileContainer>
        </>
      }
    />
    {/*<div*/}
    {/*  className={css`*/}
    {/*    display: grid;*/}
    {/*    grid-template-columns: 1fr 1fr;*/}
    {/*    grid-column-gap: 10px;*/}
    {/*    grid-row-gap: 20px;*/}

    {/*    @media (max-width: ${theme.breakpoints.sm}) {*/}
    {/*      grid-template-columns: 1fr;*/}

    {/*      > *:first-of-type {*/}
    {/*        grid-row: 2;*/}
    {/*      }*/}
    {/*    }*/}
    {/*  `}*/}
    {/*>*/}
    {/*<QRCodeElement value={props.link} />*/}
    {/*  <div*/}
    {/*    className={css`*/}
    {/*      display: grid;*/}
    {/*      grid-row-gap: 10px;*/}
    {/*      grid-auto-rows: 40px;*/}
    {/*    `}*/}
    {/*  >*/}
    {/*    <Button*/}
    {/*      variant='primary'*/}
    {/*      icon={<Icon icon={faTrashAlt} />}*/}
    {/*      iconAlign={'right'}*/}
    {/*      buttonProps={{*/}
    {/*        className: css`*/}
    {/*          opacity: 0.85;*/}
    {/*        `,*/}
    {/*        onClick: props.handleDeleteFileClick*/}
    {/*      }}*/}
    {/*      iconProps={{*/}
    {/*        className: css`*/}
    {/*          > svg {*/}
    {/*            margin: unset;*/}
    {/*            font-size: 18px;*/}
    {/*          }*/}
    {/*        `*/}
    {/*      }}*/}
    {/*      colorStates={colorVariants.dangerous}*/}
    {/*    >*/}
    {/*      Delete file*/}
    {/*    </Button>*/}
    {/*  </div>*/}
    {/*</div>*/}
    <ButtonContainer
      columns={'1fr 1fr'}
      className={css`
        grid-column-gap: 10px;
        @media (max-width: ${theme.breakpoints.sm}) {
          grid-template-columns: 1fr;
          grid-row-gap: 4px;

          // Reverse last column of three elements
          > :first-of-type {
            grid-row: 3;
          }
          > :last-of-type {
            grid-row: 1;
          }
        }
      `}
    >
      <Button
        variant='primary'
        icon={<Icon icon={faAngleLeft} />}
        iconAlign={'left'}
        buttonProps={{ onClick: props.handleGoBackButton }}
        iconProps={{
          className: css`
            font-size: 8px;
            height: 8px;
            > svg {
              margin: unset;
            }
          `
        }}
      >
        Go back
      </Button>
      <TimerCountdown
        date={props.fileUploadData ? new Date(props.fileUploadData.toDelete) : new Date()}
      />
    </ButtonContainer>
  </FileUploaded>
)

export default FileUploadedBoxView
