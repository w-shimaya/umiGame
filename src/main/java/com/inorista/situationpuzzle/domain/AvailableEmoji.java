package com.inorista.situationpuzzle.domain;

import discord4j.core.object.reaction.ReactionEmoji;

public enum AvailableEmoji {
    CROSS("U+274C"), CIRCLE("U+2B55");

    private ReactionEmoji.Unicode emoji;

    AvailableEmoji(String codepoint) {
        emoji = ReactionEmoji.Unicode.codepoints(codepoint);
    }

    public ReactionEmoji.Unicode getReactionEmoji() {
        return emoji;
    }
}
