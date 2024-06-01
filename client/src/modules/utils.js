export const formatDate = (dateString) => {
    const options = {
        day: 'numeric',
        month: 'long',
        year: 'numeric',
        hour: 'numeric',
        minute: 'numeric'
    };

    // Создаем объект Date из строки с датой
    const date = new Date(dateString);

    // Форматируем дату с помощью метода toLocaleString()
    return date.toLocaleString('ru-RU', options);
};

export const formatTime = (dateString) => {
    const date = new Date(dateString);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const formattedTime = `${hours}:${minutes}`;
    return formattedTime;
}

export const isSpaceString = (str) => {
    return !str.trim();
}
