export const handleCopy = (data) => {
  const input = document.createElement('textarea');

  input.value = data;
  document.body.appendChild(input);
  input.select();
  document.execCommand('Copy');
  document.body.removeChild(input);
};
