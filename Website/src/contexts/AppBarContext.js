import React from 'react';
import PropTypes from 'prop-types';

const AppBarContext = React.createContext();

const initialSettings = {
    title: '',
    color: 'primary',
    leftElements: null,
    rightElements: null
};

function AppBarContextProvider({ children }) {
    const [appBarSettings, setAppBarSettings] = React.useState(initialSettings);

    const customizeAppBar = changedAppBarSettings => {
        setAppBarSettings({ ...appBarSettings, ...changedAppBarSettings });
    };

    const resetAppBar = (changedAppBarSettings = null) => {
        setAppBarSettings({ ...initialSettings, ...changedAppBarSettings });
    };

    return (
        <AppBarContext.Provider
            value={{
                appBarSettings,
                customizeAppBar,
                resetAppBar
            }}
        >
            {children}
        </AppBarContext.Provider>
    );
}

AppBarContextProvider.propTypes = {
    children: PropTypes.node.isRequired,
};

export { AppBarContext, AppBarContextProvider };
