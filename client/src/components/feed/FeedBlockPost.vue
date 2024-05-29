<template>
    <feed-block-template :post="post">
        <div class="feed-preview"></div>
        <div class="feed-block-inner">
            <div class="feed-info">
                <div class="title">{{ post.title }}</div>
                <div class="description">{{ post.content }}</div>
            </div>
            <div class="feed-stats">
                <content-stats-button icon-name="eye"></content-stats-button>
                <content-stats-button :class="{ marked: reacted }" icon-name="heart" @click="toggleReaction">{{
                    (likes > 0) ? likes :
                        null }}</content-stats-button>
                <content-stats-button icon-name="comment">{{ (post.comments.length > 0) ? post.comments.length : null
                    }}</content-stats-button>
                <content-stats-button icon-name="share"></content-stats-button>
            </div>
        </div>
    </feed-block-template>
</template>

<script>
import apiClient from '@/modules/ApiClient';
import { getUserId } from '@/modules/auth';

export default {
    name: 'feed-block-post',
    props: ['post'],
    data() {
        return {
            reacted: false,
            likes: this.post.likes.length,
        }
    },
    mounted() {
        const like = this.post.likes.find(userId => userId == getUserId());
        if (like) {
            this.reacted = true;
        }
    },
    methods: {
        async toggleReaction() {
            if (this.reacted) {
                await apiClient.content.deleteReaction({
                    "post_id": this.post._id,
                    "user_id": this.post.author_id
                }, () => {
                    this.reacted = false;
                    this.likes--;
                });
            } else {
                await apiClient.content.react({
                    "post_id": this.post._id,
                    "user_id": this.post.author_id
                }, () => {
                    this.reacted = true;
                    this.likes++;
                });
            }
        }
    }
}
</script>

<style scoped>
.feed-block.light-theme .title {
    color: #af5d00;
}

.feed-block.light-theme .feed-preview {
    border: 1px #0000007c solid;
    background: #ffffff;
}

.feed-block.light-theme .feed-info {
    color: #000;
}

.feed-preview {
    aspect-ratio: 16/9;
    border: 1px #ffffff4e solid;
    border-radius: 10px;
    background: rgba(0, 0, 0, 0.5);
}

.feed-block-inner {
    flex: 1;
    display: flex;
    gap: 10px;
    flex-direction: column;
}

.feed-info {
    padding: 10px;
    position: relative;
    flex: 1;
    height: calc(100% - 45px);
}

.title {
    color: #ffbf6c;
    font-size: 22px;
    margin-bottom: 10px;
}

.description {
    text-align: justify;
    font-size: 15px;
}

.feed-stats {
    display: flex;
    gap: 10px;
}

.feed-stats-container.cost * {
    width: 100%;
    margin-bottom: 0;
}
</style>