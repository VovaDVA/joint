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
