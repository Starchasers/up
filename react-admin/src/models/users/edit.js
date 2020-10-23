import React from 'react'
import {
  TextInput,
  Edit,
  required,
  RadioButtonGroupInput,
  PasswordInput,
  Toolbar,
  SaveButton,
  DeleteButton,
  email,
  TabbedForm,
  FormTab,
  NumberInput,
  BooleanInput
} from 'react-admin'
import { makeStyles } from '@material-ui/core/styles'

const useStyles = makeStyles({
  toolbar: {
    display: 'flex',
    justifyContent: 'space-between'
  }
})

const CustomToolbar = (props) => (
  <Toolbar {...props} classes={useStyles()}>
    <SaveButton undoable={false}/>
    <DeleteButton undoable={true}/>
  </Toolbar>
)

export default (props) => (
  <Edit
    {...props}
    undoable={false}
  >
    <TabbedForm toolbar={<CustomToolbar/>}>
      <FormTab label='User Information'>
        <TextInput
          source='id'
          type='text'
          disabled
        />
        <TextInput
          source='username'
          type='text'
          validate={required()}
        />
        <TextInput
          source='email'
          type='email'
          validate={email()}
        />
        <PasswordInput
          source='password'
          type='password'
        />
        <RadioButtonGroupInput
          source='role'
          choices={[
            { id: 'USER', name: 'User' },
            { id: 'ADMIN', name: 'Admin' },
          ]}
          validate={required()}
        />
      </FormTab>
      <FormTab label='User limits'>
        <NumberInput
          source='maxTemporaryFileSize'
          label='Max temporary file size in bytes'
          validate={required()}
        />
        <NumberInput
          source='maxFileLifetime'
          label='Max file lifetime size in ms'
          validate={required()}
        />
        <NumberInput
          source='defaultFileLifetime'
          label='Default file lifetime in ms'
          validate={required()}
        />
        <BooleanInput
          source='permanentAllowed'
        />
        <NumberInput
          source='maxPermanentFileSize'
          label='Max temporary file size in bytes'
          validate={required()}
        />
      </FormTab>
    </TabbedForm>
  </Edit>
)


