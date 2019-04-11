import React from 'react';
import PropTypes from 'prop-types';
import withStyles from '@material-ui/core/styles/withStyles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import Checkbox from '@material-ui/core/Checkbox';
import WordPairAdditionContainer from '../../containers/Wordlist/WordPairAdditionContainer';
import WordPairsDeletionContainer from '../../containers/Wordlist/WordPairsDeletionContainer';
import WordPairEditingContainer from '../../containers/Wordlist/WordPairEditingContainer';
import PageContentContainer from '../PageContentContainer';

const styles = theme => ({
    list: {
        width: '100%',
        marginTop: theme.spacing.unit,
        backgroundColor: theme.palette.background.paper,
    },
});

function Wordlist({ wordPairs, checked, handleToggle, classes }) {
    return (
        <PageContentContainer>
            <WordPairAdditionContainer />
            <WordPairsDeletionContainer checked={checked} />
            <List className={classes.list}>
                {wordPairs.map(wordPair => (
                    <ListItem key={wordPair.serverId} role={undefined} button onClick={handleToggle(wordPair.serverId)}>
                        <Checkbox checked={checked.indexOf(wordPair.serverId) !== -1} tabIndex={-1} disableRipple />
                        <ListItemText primary={wordPair.wordForeign} secondary={wordPair.wordNative} />
                        <ListItemSecondaryAction>
                            <WordPairEditingContainer wordPair={wordPair} />
                        </ListItemSecondaryAction>
                    </ListItem>
                ))}
            </List>
        </PageContentContainer>
    );
}

Wordlist.propTypes = {
    wordPairs: PropTypes.array.isRequired,
    checked: PropTypes.array.isRequired,
    handleToggle: PropTypes.func.isRequired,
    classes: PropTypes.object.isRequired
};

export default withStyles(styles)(Wordlist);