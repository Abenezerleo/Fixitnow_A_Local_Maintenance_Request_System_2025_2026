export declare class TokenBlacklistService {
    private blacklistedTokens;
    addToBlacklist(token: string): void;
    isBlacklisted(token: string): boolean;
}
