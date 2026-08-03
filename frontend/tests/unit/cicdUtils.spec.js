import { DEFAULT_DEVOPS_INFO, getRepoSelectionKey, getScmDisplayName, getScmIconResource, groupByRepoNamespace } from '../../src/views/cicd/utils';

describe('CI/CD repository helpers', () => {
  it('keeps stable repository identity fields in the default form', () => {
    expect(DEFAULT_DEVOPS_INFO).toMatchObject({
      repoSelectionKey: '',
      repoId: '',
      repoPath: '',
      repoSpace: '',
      repoName: ''
    });
  });

  it('selects repositories from legacy plugins without a repository id', () => {
    expect(getRepoSelectionKey({ repoId: '101', repoUrl: 'https://git.example/group/db' })).toBe('id:101');
    expect(getRepoSelectionKey({ repoId: null, repoUrl: 'https://git.example/group/db' })).toBe('url:https://git.example/group/db');
    expect(getRepoSelectionKey({ repoId: null, repoPath: 'group/db', repoName: 'db' })).toBe('path:group/db');
  });

  it('groups duplicate project names by their full namespace', () => {
    const first = { repoId: '101', repoSpace: 'group/a', repoName: 'database' };
    const second = { repoId: '202', repoSpace: 'group/b', repoName: 'database' };
    const root = { repoId: '303', repoSpace: '', repoName: 'database' };

    expect(groupByRepoNamespace([first, second, root])).toEqual({
      'group/a': [first],
      'group/b': [second],
      '/': [root]
    });
  });

  it('uses the SCM plugin resource for provider icons', () => {
    expect(getScmIconResource('Gitlab')).toBe('webside/Gitlab@scm-icon');
    expect(getScmIconResource('Gitee')).toBe('webside/Gitee@scm-icon');
    expect(getScmIconResource()).toBe('');
  });

  it('keeps official SCM product capitalization in user-visible text', () => {
    expect(getScmDisplayName('Gitlab')).toBe('GitLab');
    expect(getScmDisplayName('Github')).toBe('GitHub');
    expect(getScmDisplayName('Gitee')).toBe('Gitee');
  });
});
