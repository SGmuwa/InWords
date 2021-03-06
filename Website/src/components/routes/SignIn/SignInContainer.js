import React from 'react';
import { useDispatch } from 'react-redux';
import useForm from 'src/hooks/useForm';
import { signIn } from 'src/actions/accessApiActions';
import SignIn from './SignIn';

function SignInContainer() {
  const dispatch = useDispatch();

  const { inputs, handleChange, handleSubmit } = useForm(
    {
      email: '',
      password: ''
    },
    () => {
      dispatch(signIn(inputs));
    }
  );

  return (
    <SignIn
      inputs={inputs}
      handleChange={handleChange}
      handleSubmit={handleSubmit}
    />
  );
}

export default SignInContainer;
