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
                <content-stats-button icon-name="comment" @click="toggleComments">{{ (post.comments.length > 0) ?
                    post.comments.length : null
                    }}</content-stats-button>
                <content-stats-button icon-name="share"></content-stats-button>
            </div>
        </div>
        <div v-if="commentVisible">
            <hr>
            <div class="comment-section">
                <user-avatar :photo="avatar"></user-avatar>
                <div class="comment-input" :class="$store.state.theme">
                    <input type="text" placeholder="Написать комментарий..." v-model="newComment"
                        @keyup.enter="sendComment">
                    <icon-button icon-name="face-smile"></icon-button>
                    <icon-button class="right" icon-name="paper-plane" @click="sendComment"></icon-button>
                </div>
            </div>
            <div class="comments">
                <post-comment v-for="comment in comments" :key="comment._id" :post="post">{{ comment.content }}</post-comment>
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
            commentVisible: false,
            newComment: '',
            comments: [],
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
        },
        async toggleComments() {
            this.commentVisible = !this.commentVisible;
            if (this.commentVisible) {
                await apiClient.content.getPostComments((data) => {
                    this.comments = data.reverse();
                }, this.post._id);
            }
        },
        async sendComment() {
            await apiClient.content.comment({
                "post_id": this.post._id,
                "author_id": this.post.author_id,
                "content": this.newComment,
            }, (data) => {
                this.newComment = '';
                this.comments.unshift(data);
            });
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

.comments {
    display: flex;
    flex-direction: column;
    gap: 15px;
    margin: 20px 10px 10px;
}

.comment-section {
    display: flex;
    gap: 10px;
    height: 40px;
}

.comment-input {
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
    padding: 5px;
    border-radius: 30px;
    background-color: rgba(0, 0, 0, 0.5);
    outline: none;
}

.comment-input input {
    flex: 1;
    border: none;
    outline: none;
    background: none;
    padding: 0 10px;
    font-size: 15px;
}

.comment-input.light-theme {
    background: #fff;
}

.comment-input.light-theme input {
    color: #000 !important;
}

.comment-input.light-theme input {
    color: #000 !important;
}

.comment-input.light-theme input::-webkit-input-placeholder {
    /* WebKit, Blink, Edge */
    color: #0000007e;
}

.comment-input.light-theme input:-moz-placeholder {
    /* Mozilla Firefox 4 to 18 */
    color: #0000007e;
    opacity: 1;
}

.comment-input.light-theme input::-moz-placeholder {
    /* Mozilla Firefox 19+ */
    color: #0000007e;
    opacity: 1;
}

.comment-input.light-theme input:-ms-input-placeholder {
    /* Internet Explorer 10-11 */
    color: #0000007e;
}

.comment-input.light-theme input::-ms-input-placeholder {
    /* Microsoft Edge */
    color: #0000007e;
}

.comment-input.light-theme input::placeholder {
    /* Most modern browsers support this now. */
    color: #0000007e;
}

.icon-button {
    padding: 0 10px;
}

hr {
    width: 100%;
    margin: 0 0 10px;
    border: none;
    border-bottom: 1px #ffffff1f solid;
}

.feed-block.light-theme hr {
    border-bottom: 1px #0000001f solid;
}


@media (max-width: 500px) {
    .comment-section {
        height: 35px;
    }

    .comment-input input {
        font-size: 13px;
    }
}
</style>